import { Component, OnInit } from '@angular/core';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-notification-toast',
  template: `
    <div *ngIf="message" class="toast">
      {{ message }}
    </div>
  `,
  styleUrls: ['./notification-toast.component.css']
})
export class NotificationToastComponent implements OnInit {
  message = '';

  constructor(private messageService: MessageService) {}

  ngOnInit() {
    this.messageService.message$.subscribe(msg => {
      this.message = msg;
    });
  }
}
