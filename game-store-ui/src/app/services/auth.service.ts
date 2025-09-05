import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Adjust this to your backend API Gateway or service URL
  private apiUrl = 'http://localhost:8085/api/auth';
  private clientId = 'game-frontend';
  
  constructor(private http: HttpClient) {}

  /**
   * Register a new user in Keycloak via the backend
   */
  register(name: string, email: string, password: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, { name, email, password }, { responseType: 'text' });
  }

  /**
   * Login a user through Keycloak (this will call your future /auth/login endpoint)
   */
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password, clientId : this.clientId }, { responseType: 'text' });
  }

  /**
   * Save JWT token locally
   */
  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  /**
   * Get JWT token from local storage
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  

  getUsernameFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const decoded: any = jwtDecode(token);
    return decoded.preferred_username || decoded.email || null;
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

  /**
   * Log out user by clearing token
   */
  logout() {
    localStorage.removeItem('token');
  }
}
