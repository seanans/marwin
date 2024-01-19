import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import { HttpClientModule } from '@angular/common/http';
import {CustomerService} from "./customer.service";

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent
    // Add other components here
  ],
  imports: [
    CommonModule,
    FormsModule,
    BrowserModule,
    HttpClientModule
    // Add other modules here
  ],
  providers: [HttpClientModule, CustomerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
