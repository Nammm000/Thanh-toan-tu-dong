import { Routes } from '@angular/router';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { RouteGuardService } from '../service/route-guard.service';
import { ManageCategoryComponent } from './manage-category/manage-category.component';
import { ManageOrderComponent } from './manage-order/manage-order.component';
import { ManageCustomerComponent } from './manage-customer/manage-customer.component';
import { ManageWarehouseComponent } from './manage-warehouse/manage-warehouse.component';
import { ManageProviderComponent } from './manage-provider/manage-provider.component';
import { ManageProductComponent } from './manage-product/manage-product.component';
import { ManageUserComponent } from './manage-user/manage-user.component';
import { ViewBillComponent } from './view-bill/view-bill.component';
import { ManageLocationComponent } from './manage-location/manage-location.component';


export const MaterialRoutes: Routes = [
    {
        path: 'category',
        component: ManageCategoryComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
    {
        path: 'product',
        component: ManageProductComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
    {
        path: 'order',
        component: ManageOrderComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN', 'USER']
        }
    },
    {
        path: 'bill',
        component: ViewBillComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN', 'USER']
        }
    },
    {
        path: 'user',
        component: ManageUserComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
    {
        path: 'customer',
        component: ManageCustomerComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
    {
        path: 'provider',
        component: ManageProviderComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
    {
        path: 'warehouse',
        component: ManageWarehouseComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
    {
        path: 'location',
        component: ManageLocationComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole: ['ADMIN']
        }
    },
];
