import { Injectable } from "@angular/core";
import { type } from "os";


export interface Menu {
    state: string;
    name: string;
    type: string;
    icon: string;
    role: string;
}

const MENUITEMS = [
    {state: 'dashboard', name: 'Dashboard', type: 'link', icon: 'dashboard', role: ''},
    {state: 'category', name: 'Manage Category', type: 'link', icon: 'category', role: 'ADMIN'},
    {state: 'product', name: 'Manage Product', type: 'link', icon: 'inventory_2', role: 'ADMIN'},
    {state: 'order', name: 'Manage Order', type: 'link', icon: 'shopping_cart', role: ''},
    {state: 'bill', name: 'View Bill', type: 'link', icon: 'backup_table', role: ''},
    {state: 'user', name: 'Manage User', type: 'link', icon: 'people', role: 'ADMIN'},
    {state: 'customer', name: 'Manage Customer', type: 'link', icon: 'face', role: 'ADMIN'},
    {state: 'provider', name: 'Manage Provider', type: 'link', icon: 'home', role: 'ADMIN'},
    {state: 'warehouse', name: 'Manage Warehouse', type: 'link', icon: 'store_mall_directory', role: 'ADMIN'},
    {state: 'location', name: 'Manage Location', type: 'link', icon: 'location_city', role: 'ADMIN'}
]

@Injectable()
export class MenuItems{
    getMenuitem(): Menu[] {
        return MENUITEMS;
    }
}