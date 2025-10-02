import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ProviderService } from 'src/app/service/provider.service';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';
// import { ProviderComponent } from '../dialog/provider/provider.component';

@Component({
  selector: 'app-manage-provider',
  // standalone: true,
  // imports: [CommonModule],
  templateUrl: './manage-provider.component.html',
  styleUrls: ['./manage-provider.component.scss']
})
export class ManageProviderComponent implements OnInit {

  displayedColumns: string[] = ['name', 'address', 'edit'];
  dataSource: any;
  //length1: any;
  responseMessage: any;


  constructor(private providerService: ProviderService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private router: Router) { }


  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();
  }

  tableData() {
    this.providerService.getProviders().subscribe((response: any) => {
      this.ngxService.stop();
      this.dataSource = new MatTableDataSource(response);
    }, (error:any) => {
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
    // const dialogRef = this.dialog.open(ProviderComponent, dialogConfig);
    // this.router.events.subscribe(() => {
    //   dialogRef.close();
    // });
    // const sub = dialogRef.componentInstance.onAddProvider.subscribe((response: any) => {
    //   this.tableData();
    // })
  }

  handleEditAction(values: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data: values
    };
    dialogConfig.width = "850px";
    // const dialogRef = this.dialog.open(ProviderComponent, dialogConfig);
    // this.router.events.subscribe(() => {
    //   dialogRef.close();
    // });
    // const sub = dialogRef.componentInstance.onEditProvider.subscribe((response: any) => {
    //   this.tableData();
    // })
  }

  handleDeleteAction(values: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete' + values.name + ' provider',
      confirmation: true
    };

    //dialogConfig.width = "850px";
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((response: any) => {
      this.ngxService.start();
      this.deleteProvider(values.id);
      dialogRef.close();
    })
  }

  deleteProvider(id: any) {
   this.providerService.delete(id).subscribe((response:any) => {
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

  onChange(status: any,id: any) {
    this.ngxService.start();
    var data = {
      status: status.toString(),
      id: id
    }
    this.providerService.updateStatus(data).subscribe((response:any) => {
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.snackbarService.openSnackBar(this.responseMessage, "Success");
    }, (error) => {
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
