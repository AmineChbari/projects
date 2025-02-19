import * as fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';
import { getContentTypeFrom } from '../utils/contentType.utils.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const BASE = 'http://localhost:8080/';
const PUBLIC_PATH = path.resolve(__dirname, '../public');
const ERROR_FILE = 'html/error.html';

/**
*  A controller for static resources
*/
export default class RequestController {

  #request;
  #response;
  #url;
  #mapRoutes;  // map of routes

  constructor(request, response) {
    this.#request = request,
    this.#response = response;
    this.#url = new URL(this.request.url, BASE).pathname;
    this.#initmapRoutes();
  }

  get response() {
    return this.#response;
  }
  get request() {
    return this.#request;
  }
  get url() {
    return this.#url;
  }

  #initmapRoutes() {
    this.#mapRoutes = new Map()
      .set('/', 'html/index.html')
      .set('/admin-vote', 'admin.html')
      .set('/votant', 'voter.html')
      .set('/about', 'html/about.html');
  }

  async handleRequest() {
    await this.buildResponse();
    this.response.end();
  }

  async buildResponse() {
    let responseFileName;
    // check if the requested resource is in the map
    if (this.#mapRoutes.has(this.url)) {
      responseFileName = this.#mapRoutes.get(this.url);
    } else { // if not found, use the url as file name
      responseFileName = this.url.substring(1); // Remove leading slash
    }
    this.response.setHeader("Content-Type", getContentTypeFrom(responseFileName));
    try {
      await this.sendFile(responseFileName);
    } catch (err) {
      await this.sendFile(ERROR_FILE);
    }
  }

  async sendFile(fileName) {
    try {
      // check if resource is available
      await fs.access(path.join(PUBLIC_PATH, fileName));
      // read the requested resource content
      const data = await fs.readFile(path.join(PUBLIC_PATH, fileName));
      // send static resource
      this.response.statusCode = 200;
      this.response.write(data);
    } catch (err) {
      // handle file not found or other errors
      this.response.statusCode = 404;
      const errorData = await fs.readFile(path.join(PUBLIC_PATH, ERROR_FILE));
      this.response.write(errorData);
    } finally {
      this.response.end();
    }
  }
}