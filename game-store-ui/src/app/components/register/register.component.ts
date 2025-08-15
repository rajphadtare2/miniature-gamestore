import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';

  constructor(private http: HttpClient) {}

  register() {
    this.http.post('http://localhost:8085/api/auth/register', {
      email: this.email,
      password: this.password,
      name: this.name
    }).subscribe(res => {
      alert('Registered Successfully');
    });
  }
}
