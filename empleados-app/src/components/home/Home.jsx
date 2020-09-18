import React, { Component } from "react";
import "./Home.css";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { EmpleadoService } from "../../service/EmpleadoService";
import "primereact/resources/themes/nova-light/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";
import { Menubar } from "primereact/menubar";
import { Panel } from "primereact/panel";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Button } from 'primereact/button';
import { Growl } from 'primereact/growl';

export default class Home extends Component {
  constructor() {
    super();
    this.state = {
      visible: false,
      empleado: {
        nombre: null,
        edad: null,
        sueldo: null,
        categoriaId: null,

      }
    };
    this.items = [
      {
        label: "Nuevo",
        icon: "pi pi-fw pi-plus",
        command: () => { this.showSaveDialog() },
      },
      {
        label: "Editar",
        icon: "pi pi-fw pi-pencil",
        comand: () => alert("Saved!"),
      },
      {
        label: "Eliminar",
        icon: "pi pi-fw pi-trash",
        comand: () => alert("Saved!"),
      },
    ];

    this.empleadoService = new EmpleadoService();
    this.save = this.save.bind(this);
    this.footer = (
      <div>
        <Button label="Guardar" icon="pi pi-check" onClick={this.save} />
      </div>
    );
  }

  componentDidMount() {
    this.empleadoService.getAll().then(data => this.setState({ empleados: data }))
  }
  save() {
    this.empleadoService.save(this.state.empleado).then(data => {
      this.setState({
        visible: false,
        empleado: {
          nombre: null,
          edad: null,
          sueldo: null,
          categoriaId: null,
        }
      });
      this.growl.show({ severity: 'success', summary: 'Atención!', detail: 'Se guardó el registro correctamente.' });
      this.empleadoService.getAll().then((data) => this.setState({ empleados: data }));

    })
  }


  render() {
    return (
      <div style={{ margin: "0px auto", width: "80%", marginTop: "20px" }} >
        <Panel header="Listado de Empleados">
          <Menubar model={this.items}></Menubar>
          <br />
          <DataTable value={this.state.empleados}>
            <Column field="id" header="Id" />
            <Column field="nombre" header="Nombre" />
            <Column field="edad" header="Edad" />
            <Column field="sueldo" header="Sueldo" />
            <Column field="nombreCategoria" header="Categoria" />
          </DataTable>
        </Panel>
        <Dialog header="Crear Empleado" visible={this.state.visible} footer={this.footer} style={{ width: "60%" }} modal={true} onHide={() => this.setState({ visible: false })}>
          <form id="empleado-form">
            <span className="p-float-label">
              <InputText value={this.state.empleado.nombre} style={{ width: "100%" }} id="nombre" onChange={(e) => {
                let val = e.target.value;
                this.setState((prevState) => {
                  let empleado = Object.assign({}, prevState.empleado);
                  empleado.nombre = val;
                  return { empleado };
                });
              }}
              />
              <label htmlFor="nombre">Nombre</label>
            </span>
            <br />
            <span className="p-float-label">
              <InputText value={this.state.empleado.edad} style={{ width: "100%" }} id="edad" onChange={(e) => {
                let val = e.target.value;
                this.setState((prevState) => {
                  let empleado = Object.assign({}, prevState.empleado);
                  empleado.edad = val;
                  return { empleado };
                });
              }}
              />
              <label htmlFor="edad">Edad</label>
            </span>
            <br />
            <span className="p-float-label">
              <InputText value={this.state.empleado.sueldo} style={{ width: "100%" }} id="sueldo" onChange={(e) => {
                let val = e.target.value;
                this.setState((prevState) => {
                  let empleado = Object.assign({}, prevState.empleado);
                  empleado.sueldo = val;
                  return { empleado };
                });
              }}
              />
              <label htmlFor="sueldo">Sueldo</label>
            </span>
            <br />
            <span className="p-float-label">
              <InputText value={this.state.empleado.categoriaId} style={{ width: "100%" }} id="categoriaId" onChange={(e) => {
                let val = e.target.value;
                this.setState((prevState) => {
                  let empleado = Object.assign({}, prevState.empleado);
                  empleado.categoriaId = val;
                  return { empleado };
                });
              }}
              />
              <label htmlFor="categoriaId">Categoria Id</label>
            </span>

          </form>
        </Dialog>
        <Growl ref={(el) => this.growl = el} />
      </div >
    );
  }
  showSaveDialog() {
    this.setState({
      visible: true,
    });
  }
}
