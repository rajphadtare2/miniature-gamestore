import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MessageService {
  private messageSubject = new Subject<string>();
  message$ = this.messageSubject.asObservable();

  show(message: string, duration = 2000) {
    this.messageSubject.next(message);
    setTimeout(() => this.messageSubject.next(''), duration);
  }
}
