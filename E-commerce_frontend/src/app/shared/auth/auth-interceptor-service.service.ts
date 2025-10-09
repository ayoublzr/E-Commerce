import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthServiceService } from './auth-service.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(
    private authService: AuthServiceService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const accessToken = this.authService.getAccessToken();
    
    // Ajouter le token seulement pour les requêtes vers notre API
    if (accessToken && request.url.includes('localhost:8087')) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    }
    
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Token expiré ou invalide - déconnecter l'utilisateur
          this.authService.logout();
          this.router.navigate(['/pages/login']);
        }
        return throwError(() => error);
      })
    );
  }
}