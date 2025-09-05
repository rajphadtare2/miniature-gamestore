import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';

  constructor(private http: HttpClient,
    private authService: AuthService
  ) {}

  register() {
    this.authService.register(this.name, this.email, this.password).subscribe({
  next: () => {
    this.authService.login( this.email,this.password ).subscribe({
      next: tokenResponse => {
        // Save token to local storage, e.g.,
        localStorage.setItem('access_token', tokenResponse.access_token);
        // Navigate to logged-in page or update UI accordingly
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
