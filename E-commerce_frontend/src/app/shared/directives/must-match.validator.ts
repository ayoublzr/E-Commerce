import {
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
  FormGroup
} from '@angular/forms';

export function MustMatch(controlName: string, matchingControlName: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const formGroup = control as FormGroup;

    const source = formGroup.get(controlName);
    const target = formGroup.get(matchingControlName);
    if (!source || !target) return null;

    // Copie des erreurs existantes pour ne pas les écraser
    const currentErrors = { ...(target.errors || {}) };

    if (source.value !== target.value) {
      currentErrors['mustMatch'] = true;
      target.setErrors(currentErrors);
      return { mustMatch: true };
    } else {
      // Retire uniquement l’erreur mustMatch si présente
      if ('mustMatch' in currentErrors) {
        delete currentErrors['mustMatch'];
        target.setErrors(Object.keys(currentErrors).length ? currentErrors : null);
      }
      return null;
    }
  };
}
