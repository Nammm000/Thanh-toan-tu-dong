import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { SubcriptionService } from 'src/app/service/subcription.service';
import { PlanService } from 'src/app/service/plan.service';

import { SnackbarService } from 'src/app/service/snackbar.service';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-subscription',
  templateUrl: './manage-subscription.component.html',
  styleUrls: ['./manage-subscription.component.scss']
})
export class ManageSubscriptionComponent implements OnInit {

  // displayedColumns: string[] = ['name', 'email', 'phone', 'status'];
  dataSource: any;
  responseMessage: any;
  listSubscription: any[] = [];

  constructor(private subcriptionService: SubcriptionService,
    private snackbarService: SnackbarService,
    private router: Router) { }

  ngOnInit(): void {
    this.setListSubscription();
  }

  setListSubscription() {
    this.subcriptionService.getSubcriptions().subscribe((response:any) => {
      // this.dataSource = new MatTableDataSource(response);
      this.listSubscription = response;
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
    // var data = {
    //   status: status.toString(),
    //   id: id
    // }
    // this.userService.updateUser(data).subscribe((response: any) => {
    //   this.responseMessage = response?.message;
    //   this.snackbarService.openSnackBar(this.responseMessage, "Success");
    // }, (error:any) => {
    //   //console.log(error.error?.message);
    //   if(error.error?.message) {
    //     this.responseMessage = error.error?.message; 
    //   } else {
    //     //alert("status is updated successfully");
    //     this.responseMessage = GlobalConstants.genericError;
    //   }
    //   this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    // })
  }

}
