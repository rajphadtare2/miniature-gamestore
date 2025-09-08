import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Game {
  gameId: number;
  name: string;
  price: number;
  devId: number;
  devName: string;
  //imageUrl?: string; //add it later
}

export interface GamePage {
  content: Game[];
  totalElements: number;
  totalPages: number;
  number: number; // current page number (0-indexed)
}

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private apiUrl = 'http://localhost:8085/api/games/allGames';

  constructor(private http: HttpClient) {}

  getAllGames(page: number = 0, size: number = 30): Observable<GamePage> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<GamePage>(this.apiUrl, { params });
  }

  getSuggestions(query: string): Observable<string[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<string[]>('http://localhost:8085/api/games/suggestGames', { params });
  }


  purchaseGame(gameId: string): Observable<any> {
    return this.http.post('http://localhost:8085/api/orders', { gameId });
  }
}