import { Routes } from '@angular/router';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { RouteGuardService } from '../service/route-guard.service';
// import { SignupComponent } from './signup/signup.component';
// import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
// import { LoginComponent } from './login/login.component';
import { ManageUserComponent } from './manage-user/manage-user.component';

import { ManageNewsComponent } from './manage-news/manage-news.component';
import { NewsComponent } from './manage-news/news/news.component';

import { ManagePlanComponent } from './manage-plan/manage-plan.component';
import { PlanComponent } from './manage-plan/plan/plan.component';

import { ManageSubscriptionComponent } from './manage-subscription/manage-subscription.component';
import { ManagePaymentComponent } from './manage-payment/manage-payment.component';

export const AdminPanelRoutes: Routes = [
  {
    path: '',
    redirectTo: '/cafe/dashboard',
    pathMatch: 'full',
  },
  {
    path: 'user',
    component: ManageUserComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'payment',
    component: ManagePaymentComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'news',
    component: ManageNewsComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'news/add',
    component: NewsComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'news/:id',
    component: NewsComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'plans',
    component: ManagePlanComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'plans/add',
    component: PlanComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'plans/:id',
    component: PlanComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'plans/:id',
    component: PlanComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  {
    path: 'subscriptions',
    component: ManageSubscriptionComponent,
    canActivate: [RouteGuardService],
    data: {
      expectedRole: ['ADMIN'],
    },
  },
  // {
  //     path: 'forgot-password',
  //     component: ForgotPasswordComponent,
  //     // canActivate: [RouteGuardService],
  //     // data: {
  //     //     expectedRole: ['ADMIN', 'USER']
  //     // }
  // }
];
