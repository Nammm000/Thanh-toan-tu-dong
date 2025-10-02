import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { LocationService } from 'src/app/service/location.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { LocationComponent } from '../dialog/location/location.component';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-location',
  // standalone: true,
  // imports: [CommonModule],
  templateUrl: './manage-location.component.html',
  styleUrls: ['./manage-location.component.scss']
})
export class ManageLocationComponent implements OnInit {

  displayedColumns: string[] = ['name', 'address', 'edit'];
  dataSource: any;
  responseMessage: any;

  constructor(
    private locationService: LocationService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();

  }

  tableData() {
    this.locationService.getLocations().subscribe((response: any) => {
      this.ngxService.stop();
      this.dataSource = new MatTableDataSource(response);
    }, (error: any) => {
      this.ngxService.stop();
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

  handleAddAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Add'
    };
    dialogConfig.width = "850px";
    const dialogRef = this.dialog.open(LocationComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
    const sub = dialogRef.componentInstance.onAddLocation.subscribe((response: any) => {
      this.tableData();
    })
  }

  handleEditAction(values: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data: values
    };
    dialogConfig.width = "850px";
    const dialogRef = this.dialog.open(LocationComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
    const sub = dialogRef.componentInstance.onAddLocation.subscribe((response: any) => {
      this.tableData();
    })
  }

  handleDeleteAction(values: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete ' + values.name + ' location',
      confirmation: true
    };

    //dialogConfig.width = "850px";
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((response: any) => {
      this.ngxService.start();
      this.deleteLocation(values.id);
      dialogRef.close();
    })
  }

  deleteLocation(id: any) {
    this.locationService.delete(id).subscribe((response:any) => {
     this.ngxService.start();
     this.tableData();
     this.responseMessage = response?.message;
     this.snackbarService.openSnackBar(this.responseMessage, "Success");
    }, (error:any) => {
     this.ngxService.stop();
     console.log(error);
     if (error.error?.message) {
       this.responseMessage = error.error?.message;
     } else {
       this.responseMessage = GlobalConstants.genericError;
     }
     this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
   }
}
