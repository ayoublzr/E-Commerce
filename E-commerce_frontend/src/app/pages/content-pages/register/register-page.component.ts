import { Component } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, Validators } from '@angular/forms';
import { AuthServiceService } from 'app/shared/auth/auth-service.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from "ngx-spinner";
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

  registerForm = new UntypedFormGroup({
    firstName:      new UntypedFormControl('', [Validators.required, Validators.minLength(2)]),
    lastName:       new UntypedFormControl('', [Validators.required, Validators.minLength(2)]),
    email:          new UntypedFormControl('', [Validators.required, Validators.email]),
    password:       new UntypedFormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword:new UntypedFormControl('', [Validators.required]),
    acceptTerms:    new UntypedFormControl(false, [Validators.requiredTrue]),
  });

  constructor(
    private router: Router,
    private authService: AuthServiceService,
    private spinner: NgxSpinnerService,
    private route: ActivatedRoute
  ) {
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

    // ⚠️ l'ordre attendu par ton service est (email, password, firstName, lastName, role)
    this.authService.register(email, password, firstName, lastName, role).subscribe({
      next: (res) => {
        this.spinner.hide();
        console.log('Registration successful', res);
      },
      error: (err) => {
        this.isRegisterFailed = true;
        this.errorMessage = err?.error?.message || 'An error occurred during registration.';
        this.spinner.hide();
        console.error('Registration error:', err);
      }
    });
  }
}
