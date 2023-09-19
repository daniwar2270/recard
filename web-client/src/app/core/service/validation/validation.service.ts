import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { FieldNamesConstants } from '../../../modules/shared/constants/fields.constants';
import { ValidationTypes } from '../../../modules/shared/constants/validation.constants';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  protected readonly fieldNames: any = FieldNamesConstants;
  protected readonly validationTypes: any = ValidationTypes;

  public checkValidations(formGroup: FormGroup, formControlName: string, errorType: string): boolean {
    const formControl: FormControl = formGroup.get(formControlName) as FormControl;
    const touchedOrDirty: boolean = formControl.touched || formControl.dirty;

    if (errorType === this.validationTypes.REQUIRED && touchedOrDirty) {
      return formControl.errors?.[this.validationTypes.REQUIRED];
    } else if (errorType === this.validationTypes.VALIDATION && touchedOrDirty) {
      return formControl.errors?.[this.validationTypes.MIN_LENGTH] || formControl.errors?.[this.validationTypes.MAX_LENGTH] || formControl.errors?.[this.validationTypes.EMAIL] || formControl.errors?.[this.validationTypes.PATTERN];
    }

    return false;
  }

  public validateDateRange(formGroup: FormGroup, formControlName: string, minDate: string, maxDate: string): boolean {
    const formControl: FormControl = formGroup.get(formControlName) as FormControl;
    const touchedOrDirty: boolean = formControl.touched || formControl.dirty;

    if (touchedOrDirty) {
      return (formControl.value < minDate) || (formControl.value > maxDate);
    }

    return false;
  }

  public checkPasswordsMatch(formGroup: FormGroup): boolean {
    const password: FormControl = formGroup.get(this.fieldNames.PASSWORD) as FormControl;
    const confirm: FormControl = formGroup.get(this.fieldNames.CONFIRM) as FormControl;
    const touchedOrDirty: boolean = confirm.touched || confirm.dirty;

    return password.value !== confirm.value && touchedOrDirty;
  }

}
