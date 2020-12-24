context('Temperature Converter', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3449')
        cy.get('#temperature-converter')
            .click()
    })

    it('should convert Celsius to Fahrenheit rounded to two decimal places', () => {
        cy.get('#celsius').click().type('1').should('have.value', '1')
        cy.get('#fahrenheit').should('have.value', '33.80')

        cy.get('#celsius').click().type('{backspace}100').should('have.value', '100')
        cy.get('#fahrenheit').should('have.value', '212.00')
    })

    it('should convert Fahrenheit to Celsius rounded to two decimal places', () => {
        cy.get('#fahrenheit').click().type('20').should('have.value', '20')
        cy.get('#celsius').should('have.value', '-6.67')

        cy.get('#fahrenheit').click().type('{backspace}{backspace}100')
            .should('have.value', '100')
        cy.get('#celsius').should('have.value', '37.78')
    })

    it('should ignore not number input', () => {
        cy.get('#celsius').click()
            .type('1')
            .type('{backspace}test').should('have.value', 'test')
        cy.get('#fahrenheit').should('have.value', '33.80')
    })
})
