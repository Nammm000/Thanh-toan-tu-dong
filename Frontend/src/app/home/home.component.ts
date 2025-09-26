import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ForgotPasswordComponent } from '../login-signup-form/forgot-password/forgot-password.component';
import { LoginComponent } from '../login-signup-form/login/login.component';
import { UserService } from '../service/user.service';
import { SignupComponent } from '../login-signup-form/signup/signup.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {

  constructor(private dialog: MatDialog, 
    private userService: UserService, 
    private router: Router) { }

  ngOnInit(): void {
  // this.userService.checkToken().subscribe((response: any) => {
  //   this.router.navigate(['/cafe/dashboard'])
  // }, (error: any) => {
  //   console.log(error);
  // })
}

  public handleSignUpAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    this.dialog.open(SignupComponent, dialogConfig);
  }

  public handleForgotPasswordAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    this.dialog.open(ForgotPasswordComponent, dialogConfig);
  }

  public handleLoginAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    this.dialog.open(LoginComponent, dialogConfig);
  }
}
