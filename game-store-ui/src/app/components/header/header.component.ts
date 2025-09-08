import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { Subscription, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { GameService } from 'src/app/services/game.service';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  username: string | null = null;
  private subscription!: Subscription;

  searchQuery = '';
  suggestions: string[] = [];
  private searchSubject = new Subject<string>();

  constructor(
    public authService: AuthService,
    private router: Router,
    private gameService: GameService
  ) {}

  ngOnInit(): void {
    this.subscription = this.authService.loggedIn$.subscribe(status => {
      this.isLoggedIn = status;
      this.username = status ? this.authService.getUsernameFromToken() : null;
    });

    this.searchSubject.pipe(
      filter(query => query.length >= 2),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => this.gameService.getSuggestions(query))
    ).subscribe(results => {
      this.suggestions = results;
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onSearchChange(): void {
    this.searchSubject.next(this.searchQuery);
  }

  selectSuggestion(suggestion: string): void {
    this.searchQuery = suggestion;
    this.suggestions = [];
    // Optionally, navigate or trigger a search for the selected game
    // Example: this.router.navigate(['/games', gameId]) if you have routes
  }
}
