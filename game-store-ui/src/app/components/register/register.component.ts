import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';

  nameFocused = false;
  emailFocused = false;
  passwordFocused = false;

  errorMessage = '';

  constructor(private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  register() {
    this.errorMessage = '';
    this.authService.register(this.name, this.email, this.password).subscribe({
    next: () => {
      this.authService.login( this.email,this.password ).subscribe({
        next: tokenResponse => {
          this.authService.saveToken(tokenResponse.access_token);
          this.authService.setLoggedIn(true); 
          this.router.navigate(['/']);
        },
        error: err => {
          console.error('Login failed', err);
        }
      });
    },
    error: err => {
      console.error('Registration failed', err);
    }
  });

  }
}
