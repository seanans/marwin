import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register/register.component'
import { HttpClientModule } from '@angular/common/http';
import { CustomerService } from './customer.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RegisterComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [HttpClientModule,CustomerService]
})
export class AppComponent {
  title = 'customer-service-angular';
}
