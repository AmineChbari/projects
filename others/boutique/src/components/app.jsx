import React from 'react';
import "../assets/style/app.css";
import ProductList from './productList.jsx';
import ShoppingCart from './shoppingCart.jsx';

/*
 define root component
*/
export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {sProduct:{}, dProduct:{}, qtyChanged:{idf:"", qx:0}};
  }

  render() {
    return (
      <div>
        <ProductList 
          productToAdd={this.productToAdd.bind(this)} 
          dProduct={this.state.dProduct} 
          qtyChanged={this.state.qtyChanged}
        />
        <ShoppingCart 
          productToDelete={this.productToDelete.bind(this)} 
          sProduct={this.state.sProduct}
          changeQty={this.changeQty.bind(this)}
        />
      </div>
    );
  }

  productToAdd(Prd) {
    this.setState({ 
      sProduct:Prd
    });
  }

  productToDelete(Prd) {
    this.setState({ 
      dProduct:Prd
    });
  }

  changeQty(id,xx) {
    this.setState({ 
      qtyChanged:{idf:id, qx:xx}
    });
  }
  
}
