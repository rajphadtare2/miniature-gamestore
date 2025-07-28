import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// Create a Game model interface (optional but recommended)
export interface Game {
  gameId: number;
  name: string;
  price: number;
  devId: number;
  devName: string;
  //imageUrl?: string; // in case you add it later
}

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private apiUrl = 'http://localhost:8083/api/games/allGames';

  constructor(private http: HttpClient) {}

  getAllGames(): Observable<Game[]> {
    return this.http.get<Game[]>(this.apiUrl);
  }

  purchaseGame(gameId: string): Observable<any> {
    //const token = localStorage.getItem('token');
    //const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post('http://localhost:8082/api/orders', { gameId });
  }
}