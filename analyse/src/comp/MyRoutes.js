import React, { Component } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";

import Home from './Home';
import Skills from './Skills';
import TechStack from './TechStack';
import Domains from './Domains';

export default class MyRoutes extends Component {
    render() {
        return (
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="home" element={<Home />} />
                    <Route path="skills" element={<Skills />} />
                    <Route path="techStack" element={<TechStack />} />
                    <Route path="domains" element={<Domains />} />
                </Routes>
            </BrowserRouter>
        )
    }
}