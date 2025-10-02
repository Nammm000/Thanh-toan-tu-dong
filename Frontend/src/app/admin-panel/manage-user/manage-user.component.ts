import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { PlanService } from 'src/app/service/plan.service';
import { SubcriptionService } from 'src/app/service/subcription.service';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';
import { UserComponent } from './user/user.component';

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
  listPlanCode: any[] = [];
  data = {
    id: '',
    email: '',
    plan_code: ''
  };

  constructor(private userService: UserService,
    private dialog: MatDialog,
    private planService: PlanService,
    private subcriptionService: SubcriptionService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router) { }

  ngOnInit(): void {
    this.setListUser();
    this.planService.getAllPlanCode().subscribe(
        (response: any) => {
          this.listPlanCode = response;
        }, (error: any) => {
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
      )
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
        message: 'delete ' + id + ' user',
        confirmation: true
      };
      const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
      const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
        (response: any) => {
          this.deleteUser(id);
          
          dialogRef.close();
        }
      );
    }
  
    deleteUser(id: any) {
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

    public handleEditAction(id: any, email: any): void {
      this.data.email = email;
      this.data.id = id;
        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = "550px";
        dialogConfig.data = {
          message: 'cập nhật người dùng ' + id,
          confirmation: true,
          listPlanCode: this.listPlanCode
        };
        const dialogRef = this.dialog.open(UserComponent, dialogConfig);
        const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
        (response: any) => {
          this.ngxService.start();
          this.updateUserPlan(response);
          
          dialogRef.close();
        }
      );
      }
    
      updateUserPlan(planCode: any) {
        this.data.plan_code = planCode;
        this.subcriptionService.add(this.data).subscribe(
          (response: any) => {
            this.ngxService.stop();
            this.responseMessage = response?.message;
            this.snackbarService.openSnackBar(this.responseMessage, 'Success');
            // this.ngxService.start();
            // this.setListUser();
          },
          (error: any) => {
            this.ngxService.stop();
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
