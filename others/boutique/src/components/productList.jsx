import React from 'react';
import "../assets/style/productList.css";
import ForSaleProduct from "./forSaleProduct.jsx";
import Filter from "./filter.jsx";
import Products from '../data/products.js';

/*
 define root component
*/
export default class ProductList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {products:Products, filterText:''};
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
      this.newQty(this.props.qtyChanged.idf,this.props.qtyChanged.qx);
    }
  }

  render() {
    const lst = this.state.products.filter(elt => (elt.name.toLowerCase()).includes(this.state.filterText.toLowerCase()))
                                    .map(element =>
      <ForSaleProduct id= {element.id}
                      name={element.name} 
                      description={element.description} 
                      weight={element.weight} 
                      image={element.image} 
                      price={element.price}
                      stock={element.stock}
                      addToCart = {this.addToCart}
                      added = {false}
                      newQty = {this.newQty}
                      key = {element.id}
                      />
    );
    return (
      <div className='productList'>
        <h4>Boutique</h4>
        <Filter filterText={this.state.filterText} filterChanged={this.filterChanged.bind(this)} />
        <div className='productsZone'>
          {lst}
        </div>
      </div>
    );
  }

  //send to cart
  addToCart(newPrd) {
    const updatedProducts = this.state.products.map(item => {
                              if (item.id === newPrd.id) {
                                if (!item.added) {
                                  this.props.productToAdd(newPrd);
                                  item.added = true;
                                  return {...item, originStock:item.stock, stock: item.stock - 1};
                                }
                              }
                              return item;
                            });
    this.setState({
      products: updatedProducts
    });
  }
  
  
  //receive from cart
  removeFromCart(Prd) {
    const updatedProducts = this.state.products.map(item => {
      if (item.id === Prd.id) {
        item.added = false;
        return {...item, stock: item.originStock };
      }
      return item;
    });
  
    this.setState({
      products: updatedProducts
    });
  }

  newQty(myId,myQty){
    const updatedProducts = this.state.products.map(item => {
      if (item.id === myId) {
        return {...item, stock: item.originStock - myQty};
      }
      return item;
    });
    this.setState({
      products : updatedProducts
    });
  }

  filterChanged(newFilterTexte) {
    this.setState({ filterText:newFilterTexte });
  }
  

}
