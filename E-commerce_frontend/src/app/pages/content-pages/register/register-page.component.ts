import { Component } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, Validators } from '@angular/forms';
import { AuthServiceService } from 'app/shared/auth/auth-service.service';
import { MustMatch } from '../../../shared/directives/must-match.validator';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent {
  registerFormSubmitted = false;
  isRegisterFailed = false;
  errorMessage: string = '';
  loading = false;

  registerForm = new UntypedFormGroup({
    firstName:      new UntypedFormControl('', [Validators.required, Validators.minLength(2)]),
    lastName:       new UntypedFormControl('', [Validators.required, Validators.minLength(2)]),
    email:          new UntypedFormControl('', [Validators.required, Validators.email]),
    password:       new UntypedFormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword:new UntypedFormControl('', [Validators.required]),
    acceptTerms:    new UntypedFormControl(false, [Validators.requiredTrue]),
  });

  constructor(private authService: AuthServiceService) {
    // applique le validator "passwords must match"
    this.registerForm.setValidators(MustMatch('password', 'confirmPassword'));
  }

  get rf() { return this.registerForm.controls; }

  onSubmit() {
    this.registerFormSubmitted = true;
    this.isRegisterFailed = false;
    this.errorMessage = '';

    if (this.registerForm.invalid) return;

    const { firstName, lastName, email, password } = this.registerForm.value as any;
    const role = 'CUSTOMER';
    this.loading = true;

    // ⚠️ l'ordre attendu par ton service est (email, password, firstName, lastName, role)
    this.authService.register(email, password, firstName, lastName, role).subscribe({
      next: () => {
        // The service redirects to the login page, which shows the "check your email" message
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.isRegisterFailed = true;
        if (err?.error?.message) {
          this.errorMessage = err.error.message;
        } else if (err?.status === 0) {
          this.errorMessage = 'Cannot reach the server. Please try again later.';
        } else {
          this.errorMessage = 'An error occurred during registration.';
        }
      }
    });
  }
}
