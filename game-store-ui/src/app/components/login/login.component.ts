import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  usernameFocused = false;
  passwordFocused = false;

  constructor(private authService: AuthService, private router: Router) {}

onSubmit() {
  this.errorMessage = '';
  this.authService.login(this.username, this.password).subscribe({
    next: (tokenResponse) => {
      const token =
        typeof tokenResponse === 'string'
          ? JSON.parse(tokenResponse)
          : tokenResponse;

      // Check for error in response even if status is 201
      if (token.error) {
        this.errorMessage = token.error_description || 'Invalid credentials';
        this.authService.setLoggedIn(false);
        return;
      }

      if (token.access_token) {
        this.authService.saveToken(token.access_token);
        this.authService.setLoggedIn(true);
        this.router.navigate(['/']);
      } else {
        this.errorMessage = 'Login failed: token missing.';
        this.authService.setLoggedIn(false);
      }
    },
    error: (err) => {
      this.errorMessage =
        err.error?.error_description || 'Login failed. Please try again.';
      this.authService.setLoggedIn(false);
    }
  });
}

}
