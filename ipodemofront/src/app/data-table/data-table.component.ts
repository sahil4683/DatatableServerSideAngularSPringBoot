import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit } from '@angular/core';
import { Subject } from 'rxjs';
import { DataTableDirective } from 'angular-datatables';
import { HttpClient, HttpParams } from '@angular/common/http';


@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.css']
})
export class DataTableComponent implements OnInit {

  constructor(private http: HttpClient) { }

  data: any[] = [];
  dtOptions: DataTables.Settings = {};
  @ViewChild(DataTableDirective, { static: false })
  datatableElement!: DataTableDirective;
  searchValues: string[] = ['', '', '', '', '', '']; // Initialize with empty strings
  search_name = '';

  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      serverSide: true,
      processing: true,
      drawCallback: (settings: any) => {
        const api = new $.fn.dataTable.Api(settings);

        api.rows().every((rowIdx: any, tableLoop: number, rowLoop: number) => {
          // let data: any = this.data();
          let data :any= api.row(rowIdx).data();
          
          // To Add Css Class
          // if (data.name === 'Ava Brown') {
          //   const cell = api.cell({ row: rowIdx, column: 1 });
          //   $(cell.node()).addClass('red-cell');
          // }

          // To A Tag
          if (data.name === 'Ava Brown') {
            const cell = api.cell({ row: rowIdx, column: 1 });
            const cellData = cell.data();
            const params = new HttpParams({ fromObject: data });
            const queryParams = params.toString();
            const link = $('<a>').attr('href', '/home?'+queryParams).text(cellData);
            cell.data(link.prop('outerHTML'));
          }

          //To Replace Text
          // if (data.name === 'Ava Brown') {
          //   const cell = api.cell({ row: rowIdx, column: 1 });
          //   const newText = 'New Text';
          //   cell.data(newText);
          // }

          // if (data.name === 'Ava Brown') {
          //   const cell = api.cell({ row: rowIdx, column: 1 });
          //   const buttonHtml = "<button type='button' onclick='getDataBySybol()'>Search</button>";
          //   cell.data(buttonHtml);
          // }

        });
      },
      ajax: (dataTablesParameters: any, callback) => {
        const params = new HttpParams()
          .set('iDisplayStart', dataTablesParameters.start)
          .set('iDisplayLength', dataTablesParameters.length)
          .set('iSortCol', dataTablesParameters.order[0].column.toString())
          .set('sSortDir', dataTablesParameters.order[0].dir)

          .set('sSearch_0', this.searchValues[0])
          .set('sSearch_1', this.searchValues[1])
          .set('sSearch_2', this.searchValues[2])
          .set('sSearch_3', this.searchValues[3])
          .set('sSearch_4', this.searchValues[4])
          .set('sSearch_5', this.searchValues[5]);

        this.http
          .get('http://localhost:8080/data', { params })
          .subscribe((response: any) => {
            this.data = response.dataArray;
            callback({
              recordsTotal: response.itotalRecords,
              recordsFiltered: response.itotalDisplayRecords,
              data: this.data
            });
          });

      },
      columns: [
        { data: 'id' },
        { data: 'name' },
        { data: 'place' },
        { data: 'city' },
        { data: 'state' },
        { data: 'phone' }
      ],
      order: [[0, 'asc']],
      language: {
        infoFiltered: ''
      },
      dom: 'C<"clear">lrtip',
      initComplete: async () => {
        const datatable = await this.datatableElement.dtInstance;
        const row = $('<tr></tr>');
        // Add search inputs to each column header
        datatable.columns().every((index) => {
          const delayTime = 300; // Adjust the delay time as needed (in milliseconds)
          const column = datatable.column(index);
          const header = $(column.header());
          const inputId = 'searchInput_' + index;
          const searchValueKey = 'searchValue' + index;
          console.log(index);


          // header.append('<br><input type="text" id="' + inputId + '" class="column-search form-control form-control-sm" placeholder="Search" [(ngModel)]="' + searchValueKey + '">');
          const cell = $('<th></th>');
          // Append the input to the cell
          if (index != 5) {
            cell.append('<input type="text" id="' + inputId + '" class="column-search form-control form-control-sm" placeholder="Search" [(ngModel)]="' + searchValueKey + '">');
          }

          // Append the cell to the row
          row.append(cell);
          // Insert the row below the header row
          header.closest('thead').after(row);

          $('input', header).on('click', function (e) {
            e.stopPropagation();
          });

          let timeout: any; // Variable to hold the timeout

          // Apply the search
          $('#' + inputId).on('keyup change', () => {
            clearTimeout(timeout); // Clear previous timeout
            timeout = setTimeout(() => {
              const value = $('#' + inputId).val() as string;
              this.searchValues[index] = value; // Store the search value in the array
              this.callBackendAPI();
            }, delayTime);
          });

        });
      }
    };
  }

   getDataBySybol() {
    console.log('calling...');
    this.searchValues[1] = this.search_name;
    this.callBackendAPI();
  }

  callBackendAPI(): void {
    this.datatableElement.dtInstance.then((datatable: DataTables.Api) => {
      datatable.clear().rows.add(this.data).draw();
    });
  }

}