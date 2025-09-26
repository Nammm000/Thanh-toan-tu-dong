import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-user',
  templateUrl: './manage-user.component.html',
  styleUrls: ['./manage-user.component.scss']
})

export class ManageUserComponent implements OnInit {

  // displayedColumns: string[] = ['name', 'email', 'phone', 'status'];
  dataSource: any;
  responseMessage: any;
  listUser: any[] = [];

  constructor(private userService: UserService,
    private dialog: MatDialog,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router) { }

  ngOnInit(): void {
    this.setListUser();
  }

  setListUser() {
    this.userService.getUser().subscribe((response:any) => {
      // this.dataSource = new MatTableDataSource(response);
      this.listUser = response;
      // console.log(this.listUser);
    }, (error:any) => {
      console.log(error.error?.message);
      if (error.error?.message) {
        this.responseMessage = error.error?.message; 
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onChange(status: any, id: any) {
    var data = {
      status: status.toString(),
      id: id
    }
    this.userService.updateUser(data).subscribe((response: any) => {
      this.responseMessage = response?.message;
      this.snackbarService.openSnackBar(this.responseMessage, "Success");
    }, (error:any) => {
      //console.log(error.error?.message);
      if(error.error?.message) {
        this.responseMessage = error.error?.message; 
      } else {
        //alert("status is updated successfully");
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  deleteAction(id: any) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.data = {
        message: 'delete ' + id + ' plan',
        condirmation: true
      };
      const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
      const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
        (response: any) => {
          this.ngxService.start();
          this.deletePlan(id);
          
          dialogRef.close();
        }
      );
    }
  
    deletePlan(id: any) {
      this.userService.delete(id).subscribe(
        (response: any) => {
          
          this.responseMessage = response?.message;
          this.snackbarService.openSnackBar(this.responseMessage, 'Success');
          this.ngxService.start();
          this.setListUser();
        },
        (error: any) => {
          if (error.error?.message) {
            this.responseMessage = error.error?.message;
          } else {
            this.responseMessage = GlobalConstants.genericError;
          }
          this.snackbarService.openSnackBar(
            this.responseMessage,
            GlobalConstants.error
          );
        }
      );
    }
    
}
