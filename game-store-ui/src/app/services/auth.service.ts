import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Adjust this to your backend API Gateway or service URL
  private apiUrl = 'http://localhost:8085/api/auth';
  private clientId = 'game-frontend';
  private loggedInSubject = new BehaviorSubject<boolean>(this.hasValidToken());
  public loggedIn$ = this.loggedInSubject.asObservable();

  constructor(private http: HttpClient) {}

  register(name: string, email: string, password: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, { name, email, password }, { responseType: 'text' });
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password, clientId : this.clientId });
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  private hasValidToken(): boolean {
    return !!this.getToken() && !this.isTokenExpired();
  }

  setLoggedIn(isLoggedIn: boolean) {
    this.loggedInSubject.next(isLoggedIn);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsernameFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const decoded: any = jwtDecode(token);
    return decoded.email || null;
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    const decoded: any = jwtDecode(token);
    const exp = decoded.exp;
    if (!exp) return true;

    const now = Math.floor(new Date().getTime() / 1000);
    return exp < now;
  }

  getCurrentLoginStatus(): boolean {
    return this.loggedInSubject.value;
  }

  logout() {
    localStorage.removeItem('token');
    this.setLoggedIn(false); 
  }
}
