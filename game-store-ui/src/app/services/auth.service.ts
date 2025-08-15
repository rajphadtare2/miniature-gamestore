import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Adjust this to your backend API Gateway or service URL
  private apiUrl = 'http://localhost:8085/auth';

  constructor(private http: HttpClient) {}

  /**
   * Register a new user in Keycloak via the backend
   */
  register(email: string, password: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, { email, password }, { responseType: 'text' });
  }

  /**
   * Login a user through Keycloak (this will call your future /auth/login endpoint)
   */
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password });
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

  /**
   * Check if user is logged in
   */
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  /**
   * Log out user by clearing token
   */
  logout() {
    localStorage.removeItem('token');
  }
}
