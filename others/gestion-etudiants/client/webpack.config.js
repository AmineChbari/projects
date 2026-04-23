import path from 'path';
import { fileURLToPath } from 'url';
import CopyWebpackPlugin from 'copy-webpack-plugin';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const PRODUCTION = true;

export default {
    mode: PRODUCTION ? 'production' : 'development',
    entry: {
        students: path.resolve(__dirname, './scripts/students.js'),
        groups: path.resolve(__dirname, './scripts/groups.js')
    },
    output: {
        path: path.resolve(__dirname, '../server/public'),
        filename: 'scripts/[name].bundle.js',
        clean: true
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'] // Handle CSS files
            },
            {
                test: /\.(png|jpe?g|gif|svg)$/i,
                type: 'asset/resource', // Handle image files
                generator: {
                    filename: 'images/[name][ext]' // Output images to the images folder
                }
            },
            {
                test: /\.(js|jsx)$/i,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                }
            }
        ]
    },
    plugins: [
        new CopyWebpackPlugin({
            patterns: [
                { from: 'style', to: 'style' }, // Copy CSS files
                { from: 'images', to: 'images' } // Copy image files
            ]
        })
    ],
    devtool: PRODUCTION ? undefined : 'eval-source-map'
};