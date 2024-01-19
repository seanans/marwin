import { Component } from '@angular/core';
import { CustomerService } from '../customer.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  customer = {
    name: '',
    email: '',
    phoneNumber: ''
  };
  isRegistered = false;
  smsCode = '';
  nameInvalid = false;
  emailInvalid = false;
  phoneInvalid = false;
  registerForm: any;

  constructor(private customerService: CustomerService) {}

  onSubmit() {
    this.customerService.createCustomer(this.customer)
      .subscribe({
        next: () => this.isRegistered = true,
        error: (error) => console.error('Error:', error)
      });
  }

  sendSMS() {
    this.customerService.sendVerificationCode(this.customer.phoneNumber)
      .subscribe({
        next: () => console.log('SMS sent'),
        error: (error) => console.error('Error:', error)
      });
  }

  submitCode() {
    this.customerService.verifyPhoneNumber(this.customer.phoneNumber, this.smsCode)
      .subscribe({
        next: () => console.log('Phone number verified'),
        error: (error) => console.error('Error:', error)
      });
  }

  validateInput(field: string) {
    if (field === 'name') {
      this.nameInvalid = !this.customer.name.match(/^[A-Za-zА-Яа-я]*$/) || this.customer.name.length > 15;
    } else if (field === 'email') {
      this.emailInvalid = !this.customer.email.match(/^\S+@\S+\.\S+$/);
    } else if (field === 'phone') {
      this.phoneInvalid = !this.customer.phoneNumber.match(/^\+380[0-9]{9}$/);
    }
  }
}
