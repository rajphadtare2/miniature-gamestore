import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})

export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  username: string | null = null;
  private subscription!: Subscription;

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.subscription = this.authService.loggedIn$.subscribe(status => {
      this.isLoggedIn = status;
      this.username = status ? this.authService.getUsernameFromToken() : null;
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  } 

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']); 
  }
}
