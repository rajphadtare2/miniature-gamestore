import { Component, OnInit, HostListener } from '@angular/core';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  games: any[] = [];
  page = 0;
  size = 30;
  totalPages = 0;
  isLoading = false;

  constructor(private gameService: GameService) {}

  ngOnInit() {
    this.loadGames(this.page);
  }

  loadGames(page: number) {
    // Prevent multiple calls or loading beyond total pages
    if (this.isLoading || (this.totalPages && page >= this.totalPages)) {
      return;
    }

    this.isLoading = true;
    this.gameService.getAllGames(page, this.size).subscribe(data => {
      this.games = [...this.games, ...data.content];
      this.totalPages = data.totalPages;
      this.page = data.number;
      this.isLoading = false;
    }, error => {
      console.error('Error loading games:', error);
      this.isLoading = false;
    });
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const threshold = 300; // px from bottom
    const position = (window.innerHeight + window.scrollY);
    const height = document.body.offsetHeight;

    if (height - position < threshold) {
      this.loadGames(this.page + 1);
    }
  }
}
