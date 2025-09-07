import { Injectable } from '@angular/core';
import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { MessageService } from 'src/app/services/message.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private authService: AuthService, private messageService: MessageService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url.includes('/login') || req.url.includes('/register')) {
            return next.handle(req).pipe(
                catchError(error => {
                    if (error.status === 401) {
                        this.authService.logout();
                        this.messageService.show('You need to login again');
                    }
                    return throwError(() => error);
                })
            );
        }
        const token = this.authService.getToken();
        let authReq = req;
        if (token) {
            authReq = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }
        return next.handle(authReq).pipe(
            catchError(error => {
                if (error.status === 401) {
                    this.authService.logout();
                    this.messageService.show('You need to login again');
                }
                return throwError(() => error);
            })
        );
    }

}
