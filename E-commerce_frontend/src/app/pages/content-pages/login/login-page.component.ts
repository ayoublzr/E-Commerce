// login-page.component.ts 

import { Component } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from "@angular/router";
import { AuthServiceService } from 'app/shared/auth/auth-service.service';
import { NgxSpinnerService } from "ngx-spinner";


@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {

  loginFormSubmitted = false;
  isLoginFailed = false;
  errorMessage: string = '';
  showPassword = false;

  loginForm = new UntypedFormGroup({
    email: new UntypedFormControl('', [Validators.required, Validators.email]),
    password: new UntypedFormControl('', [Validators.required, Validators.minLength(6)]),
    rememberMe: new UntypedFormControl(true)
  });

  constructor(
    private router: Router, 
    private authService: AuthServiceService, // Utiliser AuthServiceService
    private spinner: NgxSpinnerService,
    private route: ActivatedRoute
  ) {}

  get lf() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.loginFormSubmitted = true;
    this.isLoginFailed = false;
    this.errorMessage = '';

    if (this.loginForm.invalid) {
      return;
    }

    this.spinner.show(undefined, {
      type: 'ball-triangle-path',
      size: 'medium',
      bdColor: 'rgba(0, 0, 0, 0.8)',
      color: '#fff',
      fullScreen: true
    });

    const email = this.loginForm.value.email;
    const password = this.loginForm.value.password;

    // CORRECTION : Utiliser la méthode login() de AuthServiceService
    this.authService.login(email, password).subscribe({
      next: (res) => {
        this.spinner.hide();
        // La navigation est déjà gérée dans le service
        console.log('Login successful');
      },
      error: (err) => {
        this.isLoginFailed = true;
        this.spinner.hide();
        this.errorMessage = this.getErrorMessage(err);
        console.log('Login error:', err);
      }
    });
  }

  private getErrorMessage(error: any): string {
    if (error.error?.message) {
      return error.error.message;
    }
    if (error.status === 401) {
      return 'Invalid email or password';
    }
    if (error.status === 403) {
      return 'Account not verified. Please check your email.';
    }
    if (error.message?.includes('not verified')) {
      return 'Account not verified. Please check your email for verification link.';
    }
    return 'Login failed. Please try again.';
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}