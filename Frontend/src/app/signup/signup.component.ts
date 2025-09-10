import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../service/snackbar.service';
import { UserService } from '../service/user.service';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})

export class SignupComponent implements OnInit {

  password = true;
  confirmPassword = true;

  signUpForm: any = FormGroup;
  responseMessage: any;

  constructor(private formBuider: FormBuilder, private router: Router, 
   private userService: UserService, 
   private snacBarService: SnackbarService,
   public dialogRef: MatDialogRef<SignupComponent>,
   private ngxService: NgxUiLoaderService) { }

  ngOnInit(): void {
    this.signUpForm = this.formBuider.group({
      name: [ null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      email: [ null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
      phone: [ null, [Validators.required, Validators.pattern(GlobalConstants.phoneRegex)]],
      password: [ null, [Validators.required]],
      confirmPassword: [ null, [Validators.required]]
    })
  }

  validateSubmit() {
     if (this.signUpForm.controls['password'].value != this.signUpForm.controls['confirmPassword'].value ) {
        return true;
    } else {
      return false;
    }
  }

  handleSubmit() {
    localStorage.clear();
    this.ngxService.start();
    var formData = this.signUpForm.value;
    var data = {
     name: formData.name,
     email: formData.email,
     phone: formData.phone,
     password: formData.password
    }
    this.userService.signUp(data).subscribe((response: any) => {
       this.ngxService.stop();
       this.dialogRef.close();
       this.responseMessage = response?.message;
       this.snacBarService.openSnackBar(this.responseMessage, "");
       this.router.navigate(['/']);
    }, (error) => {
     this.ngxService.stop();
     if (error.error?.message) {
      this.responseMessage = error.error?.message;
     } else {
       this.responseMessage = GlobalConstants.genericError;
     }
      this.snacBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
     })
  }
}
