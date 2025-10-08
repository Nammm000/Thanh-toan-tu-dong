import { Component, Input } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ChangePasswordComponent } from 'src/app/material-component/dialog/change-password/change-password.component';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.scss']
})
export class ItemsComponent {
  @Input() userRole: any = "";
  @Input() name: any = "";
  
    constructor(private router: Router,
        private ngxService: NgxUiLoaderService,
        private userService: UserService,
        private dialog: MatDialog ) {
    }
  
    logoutAction() {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.data = {
          message: 'logout',
          confirmation: true
        };
        const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
        const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
          (response: any) => {
            dialogRef.close();
            this.logout();
          }
        );
      }
    
      logout() {
        this.ngxService.start();
        // localStorage.clear();
        // this.ngxService.stop();
        // this.router.navigate(['/']);
        this.userService.logout().subscribe((response: any) => {
          localStorage.clear();
          this.ngxService.stop();
          this.router.navigate(['/']);
        }, (error: any) => {
          localStorage.clear();
          this.ngxService.stop();
          this.router.navigate(['/']);
        });
      }
  
  changePassword() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    this.dialog.open(ChangePasswordComponent, dialogConfig);
  }
}
