import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {

  onEmitStatusChange = new EventEmitter();
  responseMessage: any;
  listPlanCode: any[] = [];
  planCode: any = '';

  constructor(
      @Inject(MAT_DIALOG_DATA) public dialogData: any
    ) {}

  ngOnInit(): void {
      if (this.dialogData && this.dialogData.confirmation) {
        this.listPlanCode = this.dialogData.listPlanCode;
        // console.log(this.dialogData);
      }
    }

    handleChangeAction() {
    this.onEmitStatusChange.emit(this.planCode);
  }

}
