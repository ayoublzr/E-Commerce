import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService as AuthService } from '../../../shared/auth/auth-service.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {
  email: string;
  otp: string;
  resetForm: FormGroup;
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    // email + OTP come from the router state set by the OTP verification page.
    // getCurrentNavigation() only works during navigation, history.state is the fallback.
    const state = this.router.getCurrentNavigation()?.extras.state || history.state || {};
    this.email = state['email'] || '';
    this.otp = state['otp'] || '';

    if (!this.email || !this.otp) {
      this.toastr.error('Please verify your OTP first');
      this.router.navigate(['/pages/forgotpassword']);
    }

    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    return form.get('newPassword')?.value === form.get('confirmPassword')?.value
      ? null
      : { mismatch: true };
  }

  // reset-password.component.ts
  resetPassword() {
    if (this.resetForm.invalid) {
      if (this.resetForm.get('newPassword')?.invalid) {
        this.toastr.error('Password must be at least 8 characters');
      } else if (this.resetForm.hasError('mismatch')) {
        this.toastr.error('Passwords do not match');
      }
      return;
    }

    this.loading = true;
    const newPassword = this.resetForm.get('newPassword')?.value;

    this.authService.resetPassword(this.email, this.otp, newPassword).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.status === 'success') {
          this.toastr.success(response.message || 'Password reset successfully!');
          this.router.navigate(['/pages/login']);
        } else {
          this.toastr.error(response.message || 'Password reset failed');
        }
      },
      error: (err) => {
        this.loading = false;
        this.toastr.error(err.error?.message || 'Password reset failed');
      }
    });
  }
}
