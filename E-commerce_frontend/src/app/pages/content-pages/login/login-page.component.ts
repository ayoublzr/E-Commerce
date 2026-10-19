// login-page.component.ts

import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from "@angular/router";
import { HttpErrorResponse } from '@angular/common/http';
import { AuthServiceService } from 'app/shared/auth/auth-service.service';


@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {

  loginFormSubmitted = false;
  isLoginFailed = false;
  errorMessage: string = '';
  successMessage: string = '';
  showPassword = false;
  loading = false;

  loginForm = new UntypedFormGroup({
    email: new UntypedFormControl('', [Validators.required, Validators.email]),
    password: new UntypedFormControl('', [Validators.required, Validators.minLength(6)]),
    rememberMe: new UntypedFormControl(true)
  });

  constructor(
    private authService: AuthServiceService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Messages passed in the URL by the backend (/auth/verify redirect) or by the register page
    const params = this.route.snapshot.queryParamMap;

    if (params.get('email')) {
      this.loginForm.patchValue({ email: params.get('email') });
    }

    if (params.get('verified') === 'true') {
      this.successMessage = params.get('message') || 'Account verified successfully! You can now log in.';
    } else if (params.get('verified') === 'false') {
      this.isLoginFailed = true;
      this.errorMessage = params.get('error') || 'Account verification failed.';
    } else if (params.get('registered') === 'true') {
      this.successMessage = 'Account created! Please check your email to verify your account before logging in.';
    }
  }

  get lf() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.loginFormSubmitted = true;
    this.isLoginFailed = false;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;

    const email = this.loginForm.value.email;
    const password = this.loginForm.value.password;

    this.authService.login(email, password).subscribe({
      next: () => {
        this.loading = false;
        // La navigation est déjà gérée dans le service
      },
      error: (err) => {
        this.loading = false;
        this.isLoginFailed = true;
        this.errorMessage = this.getErrorMessage(err);
      }
    });
  }

  private getErrorMessage(error: HttpErrorResponse): string {
    // The backend always answers {status, message} (GlobalExceptionHandler)
    if (error.error?.message) {
      return error.error.message;
    }
    if (error.status === 0) {
      return 'Cannot reach the server. Please try again later.';
    }
    return 'Login failed. Please try again.';
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
