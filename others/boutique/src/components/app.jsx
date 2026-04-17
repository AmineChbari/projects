import React from 'react';
import "../assets/style/app.css";
import ProductList from './productList.jsx';
import ShoppingCart from './shoppingCart.jsx';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { sProduct: {}, dProduct: {}, qtyChanged: { idf: "", qx: 0 } };
    this.productToAdd = this.productToAdd.bind(this);
    this.productToDelete = this.productToDelete.bind(this);
    this.changeQty = this.changeQty.bind(this);
  }

  render() {
    return (
      <div className='boutique-wrapper'>
        <header className='shop-header'>
          <span className='shop-header-icon'>🧸</span>
          <div className='shop-header-text'>
            <h1>La Boutique des Peluches</h1>
            <p>Peluches &amp; figurines de collection — livraison offerte dès 60&nbsp;€</p>
          </div>
        </header>
        <div className='shop-layout'>
          <ProductList
            productToAdd={this.productToAdd}
            dProduct={this.state.dProduct}
            qtyChanged={this.state.qtyChanged}
          />
          <ShoppingCart
            productToDelete={this.productToDelete}
            sProduct={this.state.sProduct}
            changeQty={this.changeQty}
          />
        </div>
      </div>
    );
  }

  productToAdd(Prd) {
    this.setState({ sProduct: Prd });
  }

  productToDelete(Prd) {
    this.setState({ dProduct: Prd });
  }

  changeQty(id, xx) {
    this.setState({ qtyChanged: { idf: id, qx: xx } });
  }
}
