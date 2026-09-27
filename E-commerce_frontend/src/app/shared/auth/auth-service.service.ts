// auth-service.service.ts - AMÉLIORÉ
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})

export class AuthServiceService {

  private apiUrl = 'http://localhost:8087/api/v1/auth';
  private tokenKey = 'ecommerce_app_tokens';
  private BASIC_URL = 'http://localhost:8087'

  private handleError(error: HttpErrorResponse) {
  console.error('HTTP Error:', error);
  return throwError(() => error);
}
 

  constructor(private http: HttpClient, private router: Router) {}

  /**
   * Méthode login principale
   */
  login(email: string, password: string): Observable<any> {
    const loginRequest = { email, password };

    return this.http.post(`${this.apiUrl}/signin`, loginRequest).pipe(
      tap((response: any) => {
        console.log('Login response:', response);
        
        const accessToken = response.accessToken;
        const refreshToken = response.refreshToken;
        const userId = response.userId;
        const role = response.role;
        const isVerified = response.isVerified;

        if (!accessToken) {
          throw new Error('No access token received');
        }

        const decodedToken: any = jwtDecode(accessToken);
        
        localStorage.setItem(this.tokenKey, JSON.stringify({
          access_token: accessToken,
          refresh_token: refreshToken,
          userId: userId,
          email: email,
          roles: [role],
          isVerified: isVerified,
          username: decodedToken.sub || email
        }));

        this.router.navigate(['/dashboard/dashboard1']);
      }),
      catchError((error) => {
        console.error('Login error:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Méthode signinUser pour compatibilité (si nécessaire)
   */
  signinUser(email: string, password: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.login(email, password).subscribe({
        next: (response) => resolve(response),
        error: (error) => reject(error)
      });
    });
  }

  /**
   * Méthode d'inscription
   */
register(email: string, password: string, firstName: string, LastName: string, role: string): Observable<any> {
    const registerRequest = { firstName, LastName, email, password, role };

    return this.http.post(`${this.apiUrl}/signup`, registerRequest).pipe(
      tap((response: any) => {
        console.log('Login response:', response);
        
        const accessToken = response.accessToken;
        const refreshToken = response.refreshToken;
        const userId = response.userId;
        const role = response.role;
        const isVerified = response.isVerified;

        if (!accessToken) {
          throw new Error('No access token received');
        }

        const decodedToken: any = jwtDecode(accessToken);
        
        localStorage.setItem(this.tokenKey, JSON.stringify({
          access_token: accessToken,
          refresh_token: refreshToken,
          userId: userId,
          email: email,
          roles: [role],
          isVerified: isVerified,
          username: decodedToken.sub || email
        }));

        this.router.navigate(['/pages/login']);
      }),
      catchError((error) => {
        console.error('Login error:', error);
        return throwError(() => error);
      })
    );
  }



  /**
   * Getters pour les informations utilisateur
   */
  getEmail(): string | null {
    const tokens = JSON.parse(localStorage.getItem(this.tokenKey) || '{}');
    return tokens.email || null;
  }

  getUsername(): string | null {
    const tokens = JSON.parse(localStorage.getItem(this.tokenKey) || '{}');
    return tokens.username || this.getEmail();
  }

  getAccessToken(): string | null {
    const tokens = JSON.parse(localStorage.getItem(this.tokenKey) || '{}');
    return tokens.access_token || null;
  }

  isAuthenticated(): boolean {
    return !!this.getAccessToken();
  }

  logout(): void {
    const tokens = this.getTokens();
    
    if (tokens?.refresh_token) {
      this.http.post(`${this.apiUrl}/logout`, { 
        refresh_token: tokens.refresh_token 
      }).subscribe({
        next: () => console.log('Logout successful'),
        error: (err) => console.error('Logout error:', err)
      });
    }
    
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/pages/login']);
  }

  private getTokens(): any {
    return JSON.parse(localStorage.getItem(this.tokenKey) || '{}');
  }

  getRoles(): string[] {
    const tokens = JSON.parse(localStorage.getItem(this.tokenKey) || '{}');
    return tokens.roles || [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  isVerified(): boolean {
    const tokens = JSON.parse(localStorage.getItem(this.tokenKey) || '{}');
    return tokens.isVerified || false;
  }

  forgotPassword(email: string): Observable<any> {
  return this.http.post(`${this.BASIC_URL}/api/v1/auth/forgot-password`, { email }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(this.handleError)
    );
  }

verifyOTP(email: string, otp: string): Observable<any> {
  return this.http.post(`${this.BASIC_URL}/api/v1/auth/verify-otp`, { email, otp }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(this.handleError)
    );
  }

resetPassword(email: string, otp: string, newPassword: string): Observable<any> {
  return this.http.post(`${this.BASIC_URL}/api/v1/auth/reset-password`, { email, otp, newPassword }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(this.handleError)
    );
  }

}