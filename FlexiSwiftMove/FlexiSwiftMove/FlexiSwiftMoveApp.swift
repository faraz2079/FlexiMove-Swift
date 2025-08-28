//
//  FlexiSwiftMoveApp.swift
//  FlexiSwiftMove
//
//  Created by Faraz on 7/30/25.
//

import SwiftUI

@main
struct FlexiSwiftMoveApp: App {
    
    var body: some Scene {
        WindowGroup {
           // ContentView()
            
            let useMock = true

                        let fetcher: VehicleFetching = useMock
                            ? MockVehicleService()
                            : VehicleService()

                        VehicleListView()
        }
    }
}
