import { BsLinkedin, BsGithub, BsInstagram } from "react-icons/bs";

function Footer() {
    return (
        <footer>

            <div>
                <a target='_blank' href='https://www.linkedin.com/in/jeandson-barros-1aa133221/' rel="noreferrer" >
                    <BsLinkedin />
                </a>
                <a target='_blank' href='https://github.com/JeandsonBarros' rel="noreferrer" >
                    <BsGithub />
                </a>
                <a target='_blank' href='https://www.instagram.com/jeandsonbarros/' rel="noreferrer">
                    <BsInstagram />
                </a>
            </div>

            <span>Created by Jeandson Barros. &copy; 2022</span>
            
        </footer>
    );
}

export default Footer;