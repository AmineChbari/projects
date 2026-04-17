import React from 'react';
import "../assets/style/productList.css";
import ForSaleProduct from "./forSaleProduct.jsx";
import Filter from "./filter.jsx";
import Products from '../data/products.js';

export default class ProductList extends React.Component {
  constructor(props) {
    super(props);
    this.state = { products: Products, filterText: '' };
    this.addToCart = this.addToCart.bind(this);
    this.removeFromCart = this.removeFromCart.bind(this);
    this.newQty = this.newQty.bind(this);
    this.filterChanged = this.filterChanged.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (prevProps.dProduct !== this.props.dProduct) {
      this.removeFromCart(this.props.dProduct);
    }
    if (prevProps.qtyChanged.qx !== this.props.qtyChanged.qx) {
      this.newQty(this.props.qtyChanged.idf, this.props.qtyChanged.qx);
    }
  }

  render() {
    const filterLower = this.state.filterText.toLowerCase();
    const lst = this.state.products
      .filter(elt => elt.name.toLowerCase().includes(filterLower))
      .map(element => (
        <ForSaleProduct
          key={element.id}
          id={element.id}
          name={element.name}
          description={element.description}
          weight={element.weight}
          image={element.image}
          price={element.price}
          stock={element.stock}
          added={element.added || false}
          addToCart={this.addToCart}
        />
      ));

    return (
      <div className='productList'>
        <h4>Catalogue ({lst.length} article{lst.length !== 1 ? 's' : ''})</h4>
        <Filter filterText={this.state.filterText} filterChanged={this.filterChanged} />
        <div className='productsZone'>
          {lst}
        </div>
      </div>
    );
  }

  addToCart(newPrd) {
    const updatedProducts = this.state.products.map(item => {
      if (item.id === newPrd.id && !item.added) {
        this.props.productToAdd(newPrd);
        return { ...item, added: true, originStock: item.stock, stock: item.stock - 1 };
      }
      return item;
    });
    this.setState({ products: updatedProducts });
  }

  removeFromCart(Prd) {
    const updatedProducts = this.state.products.map(item => {
      if (item.id === Prd.id) {
        return { ...item, added: false, stock: item.originStock };
      }
      return item;
    });
    this.setState({ products: updatedProducts });
  }

  newQty(myId, myQty) {
    const updatedProducts = this.state.products.map(item => {
      if (item.id === myId) {
        return { ...item, stock: item.originStock - myQty };
      }
      return item;
    });
    this.setState({ products: updatedProducts });
  }

  filterChanged(newFilterText) {
    this.setState({ filterText: newFilterText });
  }
}
