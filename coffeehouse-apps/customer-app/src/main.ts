import './style.css'
import App from './ui/vanilla/app'

document.querySelector<HTMLDivElement>('#app')!.innerHTML = App()
