import { Component, Input } from '@angular/core';
import { formatNumber } from 'src/app/shared/utils';

@Component({
  selector: 'app-plan-card',
  templateUrl: './plan-card.component.html',
  styleUrls: ['./plan-card.component.scss']
})
export class PlanCardComponent {

  formatNumber = formatNumber;

  @Input() plan: any;
  @Input() planNow: boolean = false;

  listDetail: any[] = [];

  ngOnInit(): void {
    this.listDetail = this.plan.description.split("\n");
    // console.log(this.plan);
  }

}
