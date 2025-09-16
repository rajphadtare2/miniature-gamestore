import { Component, OnInit, HostListener } from '@angular/core';
import { GameService } from 'src/app/services/game.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  games: any[] = [];
  page = 0;
  size = 30;
  isLoading = false;
  subscription: Subscription | null = null;

  constructor(private gameService: GameService) {}

  ngOnInit() {
    this.loadGames(this.page);
  }

  loadGames(page: number) {
    if (this.isLoading) return;

    this.isLoading = true;

    // unsubscribe previous if still running
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.subscription = this.gameService.getAllGames(page, this.size)
      .subscribe({
        next: game => {
          this.games.push(game); // push each streamed game as it arrives
        },
        error: err => {
          console.error('Error loading games:', err);
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
          this.page++; // increment page after flux completes
        }
      });
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const threshold = 300; // px from bottom
    const position = window.innerHeight + window.scrollY;
    const height = document.body.offsetHeight;

    if (height - position < threshold && !this.isLoading) {
      this.loadGames(this.page);
    }
  }
}
