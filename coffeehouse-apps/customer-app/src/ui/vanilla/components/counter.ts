function setupCounter(element: HTMLButtonElement) {
  let counter = 0
  const setCounter = (count: number) => {
    counter = count
    element.innerHTML = `count is ${counter}`
  }
  element.addEventListener('click', () => setCounter(++counter))
  setCounter(0)
}

export default class Counter extends HTMLElement {
  constructor() {
    // Always call super first in constructor
    super();

    // Render HTML
    this.innerHTML = `<button id="counter" class="flex mx-auto mt-16 text-white bg-indigo-500 border-0 py-2 px-8 focus:outline-none hover:bg-indigo-600 rounded text-lg">Button</button>`
  }
  connectedCallback() {
    setupCounter(document.querySelector<HTMLButtonElement>('#counter')!)
  }
}
