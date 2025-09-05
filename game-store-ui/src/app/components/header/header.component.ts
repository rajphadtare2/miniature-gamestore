import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
  }

  get isLoggedIn(): boolean {
    return !this.authService.isTokenExpired() && !!this.authService.getToken();
  }

  get username(): string | null {
    return this.authService.getUsernameFromToken();
  }

}
