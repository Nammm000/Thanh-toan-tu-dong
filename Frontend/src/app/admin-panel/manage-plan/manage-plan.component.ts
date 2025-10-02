import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { PlanService } from 'src/app/service/plan.service';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-plan',
  templateUrl: './manage-plan.component.html',
  styleUrls: ['./manage-plan.component.scss'],
})
export class ManagePlanComponent {
  dataSource: any;
  responseMessage: any;
  listPlans: any[] = [];

  constructor(
    private planService: PlanService,
    private dialog: MatDialog,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.ngxService.start();
    this.setListPlan();
  }

  setListPlan() {
    this.planService.getPlans().subscribe(
      (response: any) => {
        // this.dataSource = new MatTableDataSource(response);
        this.ngxService.stop();
        this.listPlans = response;
        // console.log(this.listUser);
      },
      (error: any) => {
        console.log(error.error?.message);
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

  deleteAction(id: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete ' + id + ' plan',
      confirmation: true
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
    this.planService.delete(id).subscribe(
      (response: any) => {
        
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'Success');
        this.ngxService.start();
        this.setListPlan();
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
